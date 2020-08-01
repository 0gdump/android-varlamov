package open.v0gdump.varlamov.model.reader

import open.v0gdump.varlamov.model.reader.quote.QuoteElement

data class QuotePublicationElement(
    val paragraphs: List<QuoteElement>
) : PublicationElement