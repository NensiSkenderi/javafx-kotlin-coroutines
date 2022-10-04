package javafx.ktl.coroutines.config

import kotlinx.coroutines.channels.SendChannel


enum class StepStage(val description: String) {
    STEP_1("Step 1"),
    STEP_2("Step 2"),
    STEP_3("Step 3"),
}

interface StepsLifecycle {
    object Start : StepsLifecycle
    data class Finished(val details: Map<String, String> = mapOf()) : StepsLifecycle
    data class Failed(val msg: String, val exception: Throwable) : StepsLifecycle
}

data class StageUpdate(val stage: StepStage, val state: StepsLifecycle) {
    companion object
}

class ProgressSteps(
    private val replayStageUpdateConsumer: SendChannel<StageUpdate>
) {

    suspend fun start() {
        startReplay()
    }


    private suspend fun startReplay() {
        kotlin.runCatching {
            startStep1()
            startStep2()
            startStep3()
        }
            .onFailure { }
    }


    private suspend fun startStep1() {
        replayStageUpdateConsumer.send(StepStage.STEP_1.start())
        kotlin.runCatching { Thread.sleep(3000) }
            .fold(
                onSuccess = { replayStageUpdateConsumer.send(StepStage.STEP_2.finished())},
                onFailure = { onStageFailure(StepStage.STEP_1, it) }
            )
    }

    @Suppress("CanBeParameter")
    class ReplayStageException(private val stage: StepStage, t: Throwable) :
        RuntimeException("failed to complete step ${stage.name}", t)


    private suspend fun startStep2() {
        replayStageUpdateConsumer.send(StepStage.STEP_2.start())
        val result = kotlin.runCatching {
            Thread.sleep(3000)
        }

        result.fold(
            onSuccess = { replayStageUpdateConsumer.send(StepStage.STEP_3.finished()) },
            onFailure = { onStageFailure(StepStage.STEP_2, it) }
        )
    }

    private suspend fun startStep3() {
        replayStageUpdateConsumer.send(StepStage.STEP_3.start())
        val result = kotlin.runCatching {
            Thread.sleep(3000)
        }
        result.fold(
            onSuccess = { replayStageUpdateConsumer.send(StepStage.STEP_3.finished()) },
            onFailure = { onStageFailure(StepStage.STEP_3, it) }
        )
    }

    private suspend fun onStageFailure(stage: StepStage, exception: Throwable) {
        replayStageUpdateConsumer.send(stage.failed(exception))
        throw ReplayStageException(stage, exception)
    }
}

private fun StepStage.failed(t: Throwable): StageUpdate =
    StageUpdate(this, StepsLifecycle.Failed("Failed to complete step $name", t))

private fun StepStage.finished(details: Map<String, String> = mapOf()): StageUpdate =
    StageUpdate(this, StepsLifecycle.Finished(details))

private fun StepStage.start() = StageUpdate(this, StepsLifecycle.Start)

