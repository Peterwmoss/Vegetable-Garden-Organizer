package dk.mifu.pmos.vegetablegardening

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TestUtils {
    class TestLifeCycleOwner: LifecycleOwner {
        private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }
    }

    class InstantExecutorExtension : BeforeEachCallback, AfterEachCallback {
        override fun beforeEach(context: ExtensionContext?) {
            ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) {
                    runnable.run()
                }

                override fun postToMainThread(runnable: Runnable) {
                    runnable.run()
                }

                override fun isMainThread(): Boolean {
                    return true
                }
            })
        }

        override fun afterEach(context: ExtensionContext?) {
            ArchTaskExecutor.getInstance().setDelegate(null)
        }
    }
}