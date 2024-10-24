package fr.dot.library.remote.ratp.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import fr.dot.library.remote.ratp.RatpConstant
import fr.dot.library.remote.ratp.entities.RatpWcEntity

internal class ToiletPagingSource(
    private val api: RatpApi
) : PagingSource<Int, RatpWcEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RatpWcEntity> {
        return try {
            val nextStartNumber = params.key ?: 0

            println("ToiletPagingSource nextStart: $nextStartNumber")

            val response = api.getWcs(start = nextStartNumber)
            val items = response.records

            return LoadResult.Page(
                data = response.records,
                prevKey = null,
                nextKey = if (items.size == RatpConstant.PER_PAGE) {
                    nextStartNumber.plus(RatpConstant.PER_PAGE)
                } else {
                    null
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RatpWcEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)

            anchorPage?.prevKey
                ?.plus(RatpConstant.PER_PAGE)
                ?: anchorPage?.nextKey
                    ?.minus(RatpConstant.PER_PAGE)
        }
    }

}