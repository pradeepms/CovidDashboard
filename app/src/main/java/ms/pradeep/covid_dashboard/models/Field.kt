package ms.pradeep.covid_dashboard.models

data class Field(
    val alias: String,
    val defaultValue: Any,
    val domain: Any,
    val name: String,
    val sqlType: String,
    val type: String
)