package com.rushik.cowinslotapp.frameworks

class AppHelper {
    companion object {
        fun withTrailingSlash(str: String?): String {
            if(str == null){
                return ""
            }
            return if (str.endsWith("/")) str else "$str/"
        }
    }
}