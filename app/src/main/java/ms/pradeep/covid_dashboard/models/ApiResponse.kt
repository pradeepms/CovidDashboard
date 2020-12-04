package ms.pradeep.covid_dashboard.models

data class ApiResponse(
    val exceededTransferLimit: Boolean,
    val features: List<Feature>,
    val fields: List<Field>,
    val geometryType: String,
    val globalIdFieldName: String,
    val objectIdFieldName: String,
    val spatialReference: SpatialReference,
    val uniqueIdField: UniqueIdField
)