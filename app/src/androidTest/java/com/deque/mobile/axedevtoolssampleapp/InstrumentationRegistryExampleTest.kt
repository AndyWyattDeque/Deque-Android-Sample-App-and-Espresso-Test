package com.deque.mobile.axedevtoolssampleapp

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.deque.mobile.devtools.AxeDevTools
import com.deque.mobile.devtools.ScanResultHandler
import com.deque.mobile.devtools.testingconfigs.AxeDevToolsEspressoConfig
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InstrumentationRegistryExampleTest {

    @Rule
    @JvmField
    val rule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    private val axe = AxeDevTools()

    init {
        axe.loginWithApiKey(BuildConfig.AXE_DEVTOOLS_APIKEY)
    }

    @Before
    fun setupAxeDevTools() {
        axe.tagScanAs(setOf("myAxeEspressoTest"))
        axe.setTestingConfig(
            AxeDevToolsEspressoConfig(IdlingRegistry.getInstance())
        )
        axe.setInstrumentation(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun test_using_activity() {
        rule.scenario.onActivity {
            val scanResultHandler = axe.scan(it)
            processAccessibilityResults(scanResultHandler)
        }
    }

    private fun processAccessibilityResults(scanResultHandler: ScanResultHandler?) {
        var passes = 0
        var fails = 0
        var incomplete = 0
        val result: com.deque.axe.android.AxeResult? = scanResultHandler?.serializedResult

        //      1. Upload it to the dashboard
        scanResultHandler?.uploadToDashboard()

        // 2. Save the result JSON to a local file for later use
        scanResultHandler?.saveResultToLocalStorage("axe")

//      3. Using the results in your test suite
//        var passes = 0
//        var fails = 0
//        var incomplete = 0
//        val result: AxeResult? = scanResultHandler?.serializedResult
//        result?.axeRuleResults?.forEach { ruleResult ->
//            when (ruleResult.status) {
//                AxeStatus.PASS -> {
//                    passes++
//                }
//
//                AxeStatus.FAIL -> {
//                    fails++
//                }
//
//                AxeStatus.INCOMPLETE -> {
//                    incomplete++
//                }
//            }
//        }
        //      4. Assert the results:  these may well vary by device and emulator so think carefully as to what exactly you need to assert here
//        assertEquals(6, fails)
    }

    @After
    fun tearDown() {
        axe.tearDown()
    }
}