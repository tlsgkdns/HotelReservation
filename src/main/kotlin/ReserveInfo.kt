import java.text.SimpleDateFormat
import java.time.Year
import java.util.Calendar

class ReserveInfo {
    lateinit var reserver: String
    var roomNumber: Int = 0
    var reservationKeyNumber: Int = 0
    var cost: Int = 0
    lateinit var checkOutDate: Calendar
    lateinit var checkInDate: Calendar

    constructor(_reserver: String, _roomNumber: Int, _reservationKeyNumber: Int, _checkInDate: Calendar, _checkOutDate: Calendar, _cost: Int)
    {
        reserver = _reserver
        roomNumber = _roomNumber
        reservationKeyNumber = _reservationKeyNumber
        checkInDate = _checkInDate
        checkOutDate = _checkOutDate
        cost = _cost
    }
    constructor()
    {
        reserver = ""
        roomNumber = 0
        reservationKeyNumber = 0
        checkInDate = Calendar.getInstance()
        checkOutDate = Calendar.getInstance()
    }

    fun printInformation()
    {
        println("${reservationKeyNumber}. 사용자: ${reserver}, 방번호: ${roomNumber}, 체크인: ${getDateFormat(checkInDate)}, 체크아웃: ${getDateFormat(checkOutDate)}");
    }
    private fun getDateFormat(calendar: Calendar): String
    {
        return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DATE)}";
    }
}