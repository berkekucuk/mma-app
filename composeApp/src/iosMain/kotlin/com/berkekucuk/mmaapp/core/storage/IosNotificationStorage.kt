package com.berkekucuk.mmaapp.core.storage

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.isRegisteredForRemoteNotifications

class IosNotificationStorage : NotificationStorage {
    override fun save(isEnabled: Boolean) {
        // Controlled by system
    }

    override fun load(): Boolean {
        return UIApplication.sharedApplication.isRegisteredForRemoteNotifications()
    }

    override fun openNotificationSettings() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        if (url != null) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    override fun hasRequestedPermission(): Boolean {
        return platform.Foundation.NSUserDefaults.standardUserDefaults.boolForKey("has_requested_permission")
    }

    override fun setRequestedPermission(requested: Boolean) {
        platform.Foundation.NSUserDefaults.standardUserDefaults.setBool(requested, "has_requested_permission")
    }
}
