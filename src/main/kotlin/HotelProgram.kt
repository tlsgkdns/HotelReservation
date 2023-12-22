class HotelProgram
{
    private var reservation: Reservation = Reservation()
    fun run()
    {
        println("호텔 예약자 목록입니다.")
        while(true)
        {
            println("[메뉴]")
            println("1. 방예약, 2. 예약목록 출력, 3. 예약목록(정렬) 출력, 4. 시스템 종료, 5. 금액 입금-출금 내역 목록 출력, 6. 예약 변경/취소")
            var commandNumber = 0;
            try {
                commandNumber = readln().toInt()
            } catch (e : java.lang.NumberFormatException)
            {
                println("다시 입력해주세여.")
                continue
            }
            when(commandNumber)
            {
                1 ->{reservation.runReserveProgram()}
                2 ->{reservation.printReservationInformation(false)}
                3 ->{reservation.printReservationInformation(true)}
                4 ->{
                    println("프로그램을 종료합니다.")
                    break;
                }
                5 ->{
                    reservation.printUserInfo()
                }
                6 ->{
                    reservation.modifyReservation()
                }
                else->
                {
                    println("다시 입력해주세여.")
                    continue
                }
            }
        }
    }
}