package ru.tbank.controller

import javax.servlet.http.HttpServletResponse

interface Controller {

		fun sendResponse(resp: HttpServletResponse, responseBody: String) {
				resp.contentType = "text/html"
				resp.writer.use {
						it.write(responseBody)
				}
		}
}