package com.boredream.lovebook.ui.thedaydetail

sealed class TheDayDetailCommitUiState
object CommitSuccess : TheDayDetailCommitUiState()
data class CommitFail(val reason: String) : TheDayDetailCommitUiState()