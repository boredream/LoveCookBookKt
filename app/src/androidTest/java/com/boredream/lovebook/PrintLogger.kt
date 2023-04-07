package com.boredream.lovebook

import com.boredream.lovebook.utils.Logger

class PrintLogger : Logger() {

    override fun i(log: String) {
        print("PrintLogger: $log")
    }

}