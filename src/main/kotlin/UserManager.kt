import kotlin.random.Random

class UserManager {

    private var users = mutableMapOf<String, MutableList<Int>>();

    public fun isUserContain(name: String) : Boolean
    {
        return users.contains(name);
    }
    public fun initUser(reserverName: String) : Int
    {
        val initList: MutableList<Int> = mutableListOf();
        val initMoney = Random.nextInt(10000, 1000000);
        initList += initMoney
        var cost = Random.nextInt(1, initMoney - 1)
        initList.add(-cost)
        initList.add(initMoney - cost)
        users[reserverName] = initList
        return cost
    }
    public fun addHistory(reserverName: String, money : Int): Int
    {
        val history = users[reserverName]
        var remain = history!![history.size - 1]
        var cost = money
        if(money == 0) cost = Random.nextInt(1, remain - 1)
        remain -= cost
        history!![history.size - 1] = -(cost)
        history.add(remain)
        return cost
    }

    fun printUserInfo(name: String)
    {
        val history = users[name]
        for(i in 0 .. history!!.size - 2)
        {
            val money = history[i];
            if(i == 0) println("초기 금액으로 ${money}원 입금되었습니다.")
            else if(money >= 0)
                println("${money}원 입금되었습니다.")
            else
                println("예약금으로 ${-money}원 출금되었습니다.")
        }

    }
}