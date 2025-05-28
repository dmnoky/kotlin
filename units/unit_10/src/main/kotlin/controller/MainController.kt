package controller

import ru.tbank.controller.Controller
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/*")
class MainController : HttpServlet(), Controller {
		override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
				resp.sendRedirect(req.contextPath + "/transport/all?limit=3")
		}
}