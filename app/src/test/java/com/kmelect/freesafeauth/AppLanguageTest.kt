package com.kmelect.freesafeauth

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateObserver
import org.junit.Assert.assertEquals
import org.junit.Test

class AppLanguageTest {
    @Test
    fun translationReadIsInvalidatedWhenLanguageChanges() {
        val original = AppLanguage.current
        val observer = SnapshotStateObserver { it() }
        val scope = Any()
        var invalidations = 0

        try {
            AppLanguage.current = "en"
            Snapshot.sendApplyNotifications()
            observer.start()
            // 模拟页面读取翻译，验证切换语言后 Compose 会收到刷新通知。
            observer.observeReads(scope, { invalidations++ }) {
                assertEquals("Settings", t("设置", "Settings", "設定", "설정"))
            }

            AppLanguage.current = "ko"
            Snapshot.sendApplyNotifications()

            assertEquals(1, invalidations)
            assertEquals("설정", t("设置", "Settings", "設定", "설정"))
        } finally {
            observer.stop()
            AppLanguage.current = original
        }
    }
}
