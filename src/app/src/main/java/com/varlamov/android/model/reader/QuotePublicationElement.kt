package com.varlamov.android.model.reader

data class QuotePublicationElement(
    val paragraphs: List<QuoteElement>
) : PublicationElement