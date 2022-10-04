package javafx.ktl.coroutines.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.ktl.coroutines.config.*
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.javafx.JavaFx
import java.net.URL
import java.util.*

class MainController : Initializable {

    @FXML
    private lateinit var description: Label

    @FXML
    private lateinit var successLabel: Label

    @FXML
    private lateinit var progressBar: ProgressBar

    private val mainScope = CoroutineScope(Dispatchers.JavaFx)
    private val coroutineDispatcher : CoroutineDispatcher = Dispatchers.IO

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        /*NOOP*/
    }

    @FXML
    private fun start() {
        mainScope.launch {
            val ch = Channel<StageUpdate>()

            launch {
                for (update in ch) {
                    updateProgress(update)
                }
            }

            val steps = ProgressSteps(ch)

            withContext(coroutineDispatcher) {
                steps.start()
            }
        }
    }

    private fun updateProgress(stageUpdate: StageUpdate) {
        description.text = stageUpdate.stage.description
        when (stageUpdate.state) {
            is StepsLifecycle.Start -> {
                progressBar.progress = ProgressBar.INDETERMINATE_PROGRESS
                description.text += "starting ${stageUpdate.stage.name}\n"
            }
            is StepsLifecycle.Finished -> {
                progressBar.progress = 1.0
                description.text += stageUpdate.state.details.toString() + "\n"
                if (stageUpdate.stage == StepStage.STEP_2) {
                     successLabel.isVisible = true
                }
            }
            is StepsLifecycle.Failed -> {
                progressBar.progress = 1.0
                description.text = stageUpdate.state.exception.stackTraceToString() + "\n"
                progressBar.styleClass.remove("my-progress-bar-ok")
                progressBar.styleClass.add("my-progress-bar-fail")
            }
        }
    }
}