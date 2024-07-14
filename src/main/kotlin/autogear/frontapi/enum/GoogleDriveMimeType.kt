package autogear.frontapi.enum

enum class GoogleDriveMimeType(val value: String) {
    // Google Workspace types
    FOLDER("application/vnd.google-apps.folder"),
    DOCUMENT("application/vnd.google-apps.document"),
    SPREADSHEET("application/vnd.google-apps.spreadsheet"),
    PRESENTATION("application/vnd.google-apps.presentation"),
    FORM("application/vnd.google-apps.form"),
    DRAWING("application/vnd.google-apps.drawing"),

    // Common file types
    PDF("application/pdf"),
    PLAIN_TEXT("text/plain"),
    RTF("application/rtf"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    SVG("image/svg+xml"),
    MP4("video/mp4"),
    ZIP("application/zip"),

    // Microsoft Office types
    WORD("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    POWERPOINT("application/vnd.openxmlformats-officedocument.presentationml.presentation"),

    // Other types
    AUDIO("audio/mpeg"),
    JSON("application/json"),
    HTML("text/html");

    companion object {
        fun fromValue(value: String): GoogleDriveMimeType? = values().find { it.value == value }
    }

    override fun toString(): String = value
}