package controller

import ru.tbank.controller.Controller
import ru.tbank.dto.ClientDto
import ru.tbank.exception.NotFoundException
import ru.tbank.objectMapper
import ru.tbank.service.ClientService
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/client/*")
class ClientController(
		private val clientService: ClientService = ClientService.singleton
) : HttpServlet(), Controller {
		
		override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
				val pathInfo = req.pathInfo
				try {
						val responseBody: String = when (pathInfo) {
								"/some" -> findBy(req.parameterMap)
								"/fio" -> findFIOBy(req.parameterMap)
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
				return if (params.containsKey("id")) clientService.findById(UUID.fromString(params["id"]!![0])).toString()
				else if (params.containsKey("transportId")) clientService.findByTransportId(UUID.fromString(params["transportId"]!![0])).toString()
				else throw NotFoundException("Parameter not supported!")
		}
		
		/** GET /fio?brand={brand} */
		private fun findFIOBy(params: Map<String, Array<String>>): String {
				return if (params.containsKey("brand")) clientService.getFIOByTransportBrand(params["brand"]!![0]).joinToString("<br>")
				else throw NotFoundException("Parameter not supported!")
		}
		
		/** GET /all */
		private fun findAll(): String {
				return clientService.findAll().joinToString("<br>")
		}
		
		override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
				val clientDto = objectMapper.readValue(req.inputStream, ClientDto::class.java)
				return sendResponse(resp, clientService.update(clientDto.toClient()).toString())
		}
		
		override fun doPut(req: HttpServletRequest, resp: HttpServletResponse) {
				val clientDto = objectMapper.readValue(req.inputStream, ClientDto::class.java)
				return sendResponse(resp, clientService.add(clientDto.toClient()).toString())
		}
		
		override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
				val clientDto = objectMapper.readValue(req.inputStream, ClientDto::class.java)
				return sendResponse(resp, clientService.delete(clientDto.toClient()).toString())
		}
}