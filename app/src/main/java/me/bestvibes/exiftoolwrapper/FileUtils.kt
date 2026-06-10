package me.bestvibes.exiftoolwrapper

import android.content.Intent


class FileUtils {
    companion object {
        // Android's document picker has no notion of ExifTool's format list, so
        // this allowlist approximates it. The three media wildcards cover the
        // overwhelming majority of supported files; the explicit entries cover
        // common non-media formats ExifTool also reads (PDF, archives, EPS, ...).
        // Passed via EXTRA_MIME_TYPES so the picker surfaces video/audio/etc.
        // alongside images rather than images only. See issue #9.
        private val EXIFTOOL_MIME_TYPES = arrayOf(
            "image/*",
            "video/*",
            "audio/*",
            "application/pdf",
            "application/zip",
            "application/postscript", // EPS, PS
            "application/rtf",
            "font/*",
        )

        fun makeFilePickerIntent(): Intent {
            // ACTION_OPEN_DOCUMENT (not ACTION_PICK) so the URIs come back with
            // FLAG_GRANT_READ_URI_PERMISSION + FLAG_GRANT_WRITE_URI_PERMISSION.
            // ACTION_PICK on modern Android returns Photo Picker URIs that are
            // read-only and not writeable via createWriteRequest, which forces
            // a separate permission dance for write modes; ACTION_OPEN_DOCUMENT
            // gives a unified read+write URI in a single picker.
            //
            // type must be "*/*" for EXTRA_MIME_TYPES to take effect; the
            // allowlist then narrows the picker to ExifTool-readable types
            // instead of showing every file on the device.
            return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, EXIFTOOL_MIME_TYPES)
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        }
    }
}
