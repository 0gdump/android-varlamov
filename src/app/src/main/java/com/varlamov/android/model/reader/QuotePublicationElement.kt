package com.varlamov.android.model.reader

import com.varlamov.android.model.reader.quote.QuoteElement

data class QuotePublicationElement(
    val paragraphs: List<QuoteElement>
) : PublicationElement