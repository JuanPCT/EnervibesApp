package co.com.enervibes.app.data.repository

import co.com.enervibes.app.data.api.ServiceLocator
import co.com.enervibes.app.data.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ProductsRepository {
    private val service get() = ServiceLocator.productsService

    suspend fun getProducts(
        page: Int = 1,
        limit: Int = 20,
        search: String? = null,
        categoryId: Int? = null,
        branchId: Int? = null,
        lowStock: Boolean? = null
    ): Result<Pair<List<ProductModel>, Int>> {
        return try {
            val response = service.getProducts(page, limit, search, categoryId, branchId, lowStock)
            if (response.isSuccessful) {
                val body = response.body()
                Result.success(Pair(body?.data ?: emptyList(), body?.total ?: 0))
            } else {
                Result.failure(Exception("Error al cargar productos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduct(
        name: String, price: Double, cost: Double, barcode: String?,
        descripcion: String?, categoryId: Int?, brandId: Int?, stockMinimo: Int?,
        esIngrediente: Boolean, esProductoPreparado: Boolean,
        precioPorPorcion: Double?, grupoIngrediente: String?, porciones: Int?,
        imagePart: okhttp3.MultipartBody.Part?
    ): Result<ProductModel> {
        return try {
            val mediaType = "text/plain".toMediaTypeOrNull()
            val nameBody = name.toRequestBody(mediaType)
            val priceBody = price.toString().toRequestBody(mediaType)
            val costBody = cost.toString().toRequestBody(mediaType)

            val response = service.createProduct(
                name = nameBody,
                price = priceBody,
                cost = costBody,
                barcode = barcode?.toRequestBody(mediaType),
                descripcion = descripcion?.toRequestBody(mediaType),
                categoryId = categoryId?.toString()?.toRequestBody(mediaType),
                brandId = brandId?.toString()?.toRequestBody(mediaType),
                stockMinimo = stockMinimo?.toString()?.toRequestBody(mediaType),
                esIngrediente = esIngrediente.toString().toRequestBody(mediaType),
                esProductoPreparado = esProductoPreparado.toString().toRequestBody(mediaType),
                precioPorPorcion = precioPorPorcion?.toString()?.toRequestBody(mediaType),
                grupoIngrediente = grupoIngrediente?.toRequestBody(mediaType),
                porciones = porciones?.toString()?.toRequestBody(mediaType),
                image = imagePart
            )
            if (response.isSuccessful) {
                val body = response.body()
                body?.data?.let { Result.success(it) }
                    ?: Result.failure(Exception(body?.message ?: "Error al crear producto"))
            } else {
                Result.failure(Exception("Error del servidor"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(id: Int): Result<Unit> {
        return try {
            val response = service.deleteProduct(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setInitialStock(
        productId: Int, branchId: Int, quantity: Double, minStockLevel: Double?
    ): Result<Unit> {
        return try {
            val response = service.setInitialStock(productId, branchId, quantity, minStockLevel)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al establecer stock"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
