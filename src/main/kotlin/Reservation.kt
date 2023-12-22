import java.time.Year
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

class Reservation {
    private var reservations = mutableListOf<ReserveInfo>();
    private var roomKeyNumber = 1
    private var userManager: UserManager = UserManager()
    fun runReserveProgram()
    {
        println("예약자분의 성함을 입력해주세요")
        val reserverName: String = readln()
        val reserveRoom = getReserveRoomNumber()
        val checkInDate = getCheckInDate(reserveRoom)
        val checkOutDate: Calendar = getCheckOutDate(reserveRoom, checkInDate)
        var cost = 0
        if(!userManager.isUserContain(reserverName))
            cost = userManager.initUser(reserverName)
        else
            cost = userManager.addHistory(reserverName, 0)
        reservations.add(ReserveInfo(reserverName, reserveRoom, roomKeyNumber++, checkInDate, checkOutDate, cost))
        println("호텔 예약이 완료되었습니다.")
    }
    fun modifyReservation(): Boolean
    {
        println("조회하실 사용자 이름을 입력하세요.")
        val name = readln()
        if(!userManager.isUserContain(name))
        {
            println("예약된 사용자를 찾을 수 없습니다.")
            return false
        }
        while (true)
        {
            println("${name} 님이 예약한 목록입니다. 변경하실 예약번호를 입력해주세요 (탈출은 exit입력)")
            for(reserve in reservations)
                if(reserve.reserver == name)
                    reserve.printInformation()
            val command = readln()
            var num = 0
            if(command == "exit") return false
            try {
                num = command.toInt()
            } catch (e : java.lang.NumberFormatException)
            {
                println("잘못된 입력입니다.")
                continue
            }
            var selectedReserve: ReserveInfo = ReserveInfo()
            for(reserve in reservations)
                if(num == reserve.reservationKeyNumber)
                    selectedReserve = reserve
            if(selectedReserve.reservationKeyNumber == 0)
            {
                println("범위에 없는 예약번호입니다.")
                continue;
            }
            println("해당 예약을 어떻게 하시겠어요 1. 변경 2. 취소 / 이외 번호. 메뉴로 돌아가기")
            try
            {
                num = readln().toInt()
            } catch (e : java.lang.NumberFormatException)
            {
                return false;
            }
            if(num != 1 && num != 2) return false
            if(num == 1)
            {
                reservations.remove(selectedReserve)
                selectedReserve.checkInDate = getCheckInDate(selectedReserve.roomNumber)
                selectedReserve.checkOutDate = getCheckOutDate(selectedReserve.roomNumber, selectedReserve.checkInDate)
                reservations.add(selectedReserve)
                break
            }
            else
            {
                printCancelInfo()
                var rate = 1.0
                val three = Calendar.getInstance(); val five = Calendar.getInstance(); val seven = Calendar.getInstance(); val fourteen = Calendar.getInstance();
                three.add(Calendar.DATE, 3); five.add(Calendar.DATE, 5); seven.add(Calendar.DATE, 7); fourteen.add(Calendar.DATE, 14)
                if(selectedReserve.checkInDate.before(three)) rate = 0.0;
                else if(selectedReserve.checkInDate.before(five)) rate = 0.3;
                else if(selectedReserve.checkInDate.before(seven)) rate = 0.5;
                else if(selectedReserve.checkInDate.before(fourteen)) rate = 0.8;
                else rate = 1.0;
                if(rate > 0.0) userManager.addHistory(selectedReserve.reserver, -(selectedReserve.cost.toDouble() * rate).toInt())
                reservations.remove(selectedReserve)
                println("취소가 완료되었습니다.")
                break
            }
        }
        return true
    }
    private fun printCancelInfo()
    {
        println("[취소 유의사항]")
        println("체크인 3일 이전 취소 예약금 환불 불가")
        println("체크인 5일 이전 취소 예약금의 30% 환불")
        println("체크인 7일 이전 취소 예약금의 50% 환불")
        println("체크인 14일 이전 취소 예약금의 80% 환불")
        println("체크인 30일 이전 취소 예약금의 100% 환불")
    }
    fun printUserInfo()
    {
        println("조회하실 사용자 이름을 입력하세요.")
        val name = readln()
        if(!userManager.isUserContain(name))
            println("예약된 사용자를 찾을 수 없습니다.")
        else
            userManager.printUserInfo(name)
    }
    private fun canCheckIn(roomNumber: Number, date: Calendar) : Boolean
    {
        for(reserve in reservations)
            if(reserve.roomNumber == roomNumber &&
                (equalDate(date, reserve.checkInDate) || equalDate(date, reserve.checkOutDate)
                        || (reserve.checkInDate.before(date) && reserve.checkOutDate.after(date))))
                return false
        return true
    }
    private fun canCheckOut(roomNumber: Number, date: Calendar, date2: Calendar) : Boolean
    {
        for(reserve in reservations)
            if(reserve.roomNumber == roomNumber)
            {
                return date.after(reserve.checkOutDate) || date2.before(reserve.checkInDate)
            }
        return true
    }
    fun printReservationInformation(sorted: Boolean)
    {
        if(sorted) reservations.sortBy { it.checkInDate}
        for(reserve in reservations)
            reserve.printInformation()
    }
    private fun getReserveRoomNumber(): Int
    {
        var reserveRoomNumber: Int = 0;
        println("예약할 방번호를 입력해주세요")
        while (true)
        {
            try {
                reserveRoomNumber = readln().toInt()
            } catch (e : java.lang.NumberFormatException)
            {
                println("올바르지 않은 방번호입니다. 방번호는 100~999 영역 이내입니다.")
            }
            if(reserveRoomNumber !in 100 .. 999)
            {
                println("올바르지 않은 방번호입니다. 방번호는 100~999 영역 이내입니다.")
                continue
            }
            break
        }
        return reserveRoomNumber;
    }
    private fun getCheckInDate(roomNumber: Int): Calendar
    {
        while(true)
        {
            println("체크인 날짜를 입력해주세요 표기형식. 20231206")
            val inputDate = readln()
            val parsedDate = parseDate(inputDate)
            if(parsedDate.get(Calendar.YEAR) == 19000) continue
            if(parsedDate.before(Calendar.getInstance()))
            {
                println("체크인은 지난날을 선택할 수 없습니다.")
                continue
            }
            if(!canCheckIn(roomNumber, parsedDate))
            {
                println("해당 날짜에 이미 방을 사용중입니다. 다른날짜를 입력해주세요.")
                continue
            }
            return parsedDate
        }
    }
    private fun parseDate(dateString: String): Calendar
    {
        var intDate: Int = 0
        if(dateString.length != 8) {
            println("형식이 잘못되었습니다.")
            return Calendar.getInstance().let {
                it.set(Calendar.YEAR, 19000)
                it
            }
        }
        try {
            intDate = dateString.toInt()
        } catch (e : java.lang.NumberFormatException)
        {
            println("형식이 잘못되었습니다.")
            return Calendar.getInstance().let {
                it.set(Calendar.YEAR, 19000)
                it
            }
        }
        val calender: Calendar = Calendar.getInstance()
        calender.set(Calendar.YEAR, intDate / 10000)
        calender.set(Calendar.MONTH, ((intDate % 10000) / 100) - 1)
        calender.set(Calendar.DATE, intDate % 100)
        return calender
    }
    private fun getCheckOutDate(roomNumber: Int, checkInDate: Calendar): Calendar
    {
        while(true)
        {
            println("체크아웃 날짜를 입력해주세요 표기형식. 20231206")
            val inputDate = readln()
            val parsedDate = parseDate(inputDate)
            if(parsedDate.get(Calendar.YEAR) == 19000) continue
            if(parsedDate.before(checkInDate)
                || equalDate(checkInDate, parsedDate))
            {
                println("체크인 날짜보다 이전이거나 같을 수 없습니다.")
                continue
            }
            if(!canCheckOut(roomNumber, checkInDate, parsedDate))
            {
                println("해당 날짜에 이미 방을 사용중입니다. 다른날짜를 입력해주세요.")
                continue
            }
            return parsedDate
        }
    }
    private fun equalDate(c1: Calendar, c2: Calendar): Boolean
    {
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && (c1.get(Calendar.DATE) == c2.get(Calendar.DATE)))
    }
}