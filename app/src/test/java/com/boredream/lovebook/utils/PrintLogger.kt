package com.boredream.lovebook.utils

class PrintLogger : Logger() {

    override fun i(log: String) {
        print(log)
    }

}