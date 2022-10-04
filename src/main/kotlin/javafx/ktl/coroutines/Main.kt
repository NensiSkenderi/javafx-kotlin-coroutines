package javafx.ktl.coroutines

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {

    //VM args --module-path /Users/Nensi.Skenderi/Downloads/javafx-sdk-19/lib --add-modules javafx.controls,javafx.fxml,javafx.web

    override fun start(stage: Stage) {
        var root: Parent = FXMLLoader.load(javaClass.classLoader.getResource("fxml/Main.fxml"))
        val scene = Scene(root)
        stage.isResizable = true
        stage.scene = scene
        stage.title = "Main View"
        stage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}