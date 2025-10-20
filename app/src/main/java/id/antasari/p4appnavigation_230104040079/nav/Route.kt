package id.antasari.p4appnavigation_230104040079.nav

sealed class Route(val path: String) {
    data object Home : Route("home")

    data object ActivityA : Route("activity_a")
    data object ActivityB : Route("activity_b")
    data object ActivityC : Route("activity_c")

    data object ActivityD : Route("activity_d/{name}/{studentId}") {
        fun make(name: String, studentId: String): String =
            "activity_d/${name.urlEnc()}/${studentId.urlEnc()}"
    }

    data object Step1 : Route("step_1")
    data object Step2 : Route("step_2")
    data object Step3 : Route("step_3")

    data object Hub : Route("hub")
    data object HubDashboard : Route("hub/dashboard")
    data object HubMessages : Route("hub/messages")
    data object HubProfile : Route("hub/profile")

    // ✅ PERUBAHAN DI SINI
    data object HubMsgDetail : Route("hub/messages/detail/{id}") {
        fun of(id: String) = "hub/messages/detail/$id"
    }
}

private fun String.urlEnc() = java.net.URLEncoder.encode(this, "utf-8")