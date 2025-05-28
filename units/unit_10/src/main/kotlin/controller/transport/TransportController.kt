package ru.tbank.controller.transport

import ru.tbank.controller.Controller
import ru.tbank.dto.TransportDto
import ru.tbank.exception.NotFoundException
import ru.tbank.objectMapper
import ru.tbank.service.TransportService
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/transport/*")
class TransportController(
		private val transportService: TransportService = TransportService.singleton
) : HttpServlet(), Controller {
		
		override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
				val pathInfo = req.pathInfo
				try {
						val responseBody: String = when (pathInfo) {
								"/some" -> findBy(req.parameterMap)
								"/all" -> findAll(req.parameterMap)
								else -> throw NotFoundException("Path $pathInfo not supported! <br>${req.requestURL}")
						}
						sendResponse(resp, responseBody)
				} catch (e: NotFoundException) {
						resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.message)
				}
		}
		
		/** GET /some?id={UUID} */
		private fun findBy(params: Map<String, Array<String>>): String {
				return if (params.containsKey("id")) transportService.findById(UUID.fromString(params["id"]!![0])).toString()
				else if (params.containsKey("clientId")) transportService.findByClientId(UUID.fromString(params["clientId"]!![0])).toString()
				else throw NotFoundException("Parameter not supported!")
		}
		
		/** GET /all */
		private fun findAll(params: Map<String, Array<String>>): String {
				return if (params.containsKey("brand")) transportService.findByBrand(params["brand"]!![0]).joinToString("<br>")
				else if (params.containsKey("limit")) transportService.findAllByLimit(params["limit"]!![0].toInt()).joinToString("<br>")
				else transportService.findAll().joinToString("<br>")
		}
		
		override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
				val transportDto = objectMapper.readValue(req.inputStream, TransportDto::class.java)
				return sendResponse(resp, transportService.update(transportDto.toTransport()).toString())
		}
		
		override fun doPut(req: HttpServletRequest, resp: HttpServletResponse) {
				val transportDto = objectMapper.readValue(req.inputStream, TransportDto::class.java)
				return sendResponse(resp, transportService.add(transportDto.toTransport()).toString())
		}
		
		override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
				val transportDto = objectMapper.readValue(req.inputStream, TransportDto::class.java)
				return sendResponse(resp, transportService.delete(transportDto.toTransport()).toString())
		}
}