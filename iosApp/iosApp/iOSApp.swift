import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        InitKoinIOSKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}