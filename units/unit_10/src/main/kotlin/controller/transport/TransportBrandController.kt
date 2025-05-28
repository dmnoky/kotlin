package ru.tbank.controller.transport

import ru.tbank.controller.Controller
import ru.tbank.dto.transport.TransportBrandDto
import ru.tbank.exception.NotFoundException
import ru.tbank.objectMapper
import ru.tbank.service.TransportBrandService
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet("/transport/brand/*")
class TransportBrandController(
		private val transportBrandService: TransportBrandService = TransportBrandService.singleton
) : HttpServlet(), Controller {
		
		override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
				val pathInfo = req.pathInfo
				try {
						val responseBody: String = when (pathInfo) {
								"/some" -> findBy(req.parameterMap)
								"/all" -> findAll()
								else -> throw NotFoundException("Path $pathInfo not supported! <br>${req.requestURL}")
						}
						sendResponse(resp, responseBody)
				} catch (e: NotFoundException) {
						resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.message)
				}
		}
		
		/** GET /some?id={UUID} */
		private fun findBy(params: Map<String, Array<String>>): String {
				return if (params.containsKey("id")) transportBrandService.findById(UUID.fromString(params["id"]!![0])).toString()
				else if (params.containsKey("name")) transportBrandService.findByName(params["name"]!![0]).toString()
				else throw NotFoundException("Parameter not supported!")
		}
		
		/** GET /all */
		private fun findAll(): String {
				return transportBrandService.findAll().joinToString("<br>")
		}
		
		override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
				val dto = objectMapper.readValue(req.inputStream, TransportBrandDto::class.java)
				return sendResponse(resp, transportBrandService.update(dto.toEntity()).toString())
		}
		
		override fun doPut(req: HttpServletRequest, resp: HttpServletResponse) {
				val dto = objectMapper.readValue(req.inputStream, TransportBrandDto::class.java)
				return sendResponse(resp, transportBrandService.add(dto.toEntity()).toString())
		}
		
		override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
				val dto = objectMapper.readValue(req.inputStream, TransportBrandDto::class.java)
				return sendResponse(resp, transportBrandService.delete(dto.toEntity()).toString())
		}
}