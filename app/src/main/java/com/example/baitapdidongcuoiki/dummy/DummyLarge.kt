package com.example.baitapdidongcuoiki.dummy

import kotlinx.coroutines.delay

// ===================== ENTITY =====================
data class Transaction(
    val id: Int,
    val amount: Double,
    val type: String,
    val description: String,
    val timestamp: Long
)

data class SmsMessage(
    val sender: String,
    val content: String,
    val time: Long
)

// ===================== DAO =====================
interface TransactionDao {
    fun getAll(): List<Transaction>
    fun insert(transaction: Transaction)
    fun getTotal(): Double
}

class FakeTransactionDao : TransactionDao {

    private val data = mutableListOf<Transaction>()

    override fun getAll(): List<Transaction> = data

    override fun insert(transaction: Transaction) {
        data.add(transaction)
    }

    override fun getTotal(): Double {
        return data.sumOf { it.amount }
    }
}

// ===================== API =====================
interface ApiService {
    suspend fun fetchTransactions(): List<Transaction>
}

class FakeApiService : ApiService {
    override suspend fun fetchTransactions(): List<Transaction> {
        delay(500)
        return listOf(
            Transaction(1, 100.0, "INCOME", "Salary", System.currentTimeMillis()),
            Transaction(2, -50.0, "EXPENSE", "Food", System.currentTimeMillis())
        )
    }
}

// ===================== REPOSITORY =====================
class TransactionRepository(
    private val dao: TransactionDao,
    private val api: ApiService
) {
    suspend fun refresh(): List<Transaction> {
        val remote = api.fetchTransactions()
        remote.forEach { dao.insert(it) }
        return dao.getAll()
    }

    fun getTotal(): Double = dao.getTotal()
}

// ===================== SMS PARSER =====================
object SmsParser {

    fun parse(message: SmsMessage): Transaction? {
        return if (message.content.contains("received")) {
            Transaction(
                id = message.hashCode(),
                amount = 200.0,
                type = "INCOME",
                description = message.content,
                timestamp = message.time
            )
        } else null
    }
}

// ===================== USE CASE =====================
class RefreshTransactionsUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): List<Transaction> {
        return repository.refresh()
    }
}

// ===================== DUMMY DATA =====================
object DummyData {

    val transactions = listOf(
        Transaction(1, 100.0, "INCOME", "Salary", 1710000000000),
        Transaction(2, -20.0, "EXPENSE", "Coffee", 1710000001000),
        Transaction(3, -150.0, "EXPENSE", "Shopping", 1710000002000),
        Transaction(4, 500.0, "INCOME", "Freelance", 1710000003000),
        Transaction(5, -70.0, "EXPENSE", "Transport", 1710000004000),
        Transaction(6, -30.0, "EXPENSE", "Food", 1710000005000),
        Transaction(7, 200.0, "INCOME", "Bonus", 1710000006000),
        Transaction(8, -120.0, "EXPENSE", "Bills", 1710000007000),
        Transaction(9, -15.0, "EXPENSE", "Snacks", 1710000008000),
        Transaction(10, 300.0, "INCOME", "Gift", 1710000009000),
        Transaction(11, -25.0, "EXPENSE", "Breakfast", 1710000010000),
        Transaction(12, -60.0, "EXPENSE", "Lunch", 1710000011000),
        Transaction(13, -120.0, "EXPENSE", "Dinner", 1710000012000),
        Transaction(14, 1500.0, "INCOME", "Salary", 1710000013000),
        Transaction(15, -200.0, "EXPENSE", "Shopping", 1710000014000),
        Transaction(16, -80.0, "EXPENSE", "Taxi", 1710000015000),
        Transaction(17, 300.0, "INCOME", "Freelance", 1710000016000),
        Transaction(18, -45.0, "EXPENSE", "Coffee", 1710000017000),
        Transaction(19, -500.0, "EXPENSE", "Rent", 1710000018000),
        Transaction(20, 100.0, "INCOME", "Gift", 1710000019000),

        Transaction(21, -70.0, "EXPENSE", "Groceries", 1710000020000),
        Transaction(22, -30.0, "EXPENSE", "Snacks", 1710000021000),
        Transaction(23, 200.0, "INCOME", "Bonus", 1710000022000),
        Transaction(24, -150.0, "EXPENSE", "Clothes", 1710000023000),
        Transaction(25, -20.0, "EXPENSE", "Parking", 1710000024000),
        Transaction(26, 400.0, "INCOME", "Side Job", 1710000025000),
        Transaction(27, -90.0, "EXPENSE", "Fuel", 1710000026000),
        Transaction(28, -60.0, "EXPENSE", "Cinema", 1710000027000),
        Transaction(29, -15.0, "EXPENSE", "Water", 1710000028000),
        Transaction(30, 250.0, "INCOME", "Commission", 1710000029000),

        Transaction(31, -300.0, "EXPENSE", "Hotel", 1710000030000),
        Transaction(32, -50.0, "EXPENSE", "Internet", 1710000031000),
        Transaction(33, 700.0, "INCOME", "Project", 1710000032000),
        Transaction(34, -40.0, "EXPENSE", "Laundry", 1710000033000),
        Transaction(35, -25.0, "EXPENSE", "Milk Tea", 1710000034000),
        Transaction(36, 120.0, "INCOME", "Refund", 1710000035000),
        Transaction(37, -180.0, "EXPENSE", "Electronics", 1710000036000),
        Transaction(38, -35.0, "EXPENSE", "Breakfast", 1710000037000),
        Transaction(39, -90.0, "EXPENSE", "Dinner", 1710000038000),
        Transaction(40, 500.0, "INCOME", "Freelance", 1710000039000),

        Transaction(41, -45.0, "EXPENSE", "Coffee", 1710000040000),
        Transaction(42, -120.0, "EXPENSE", "Shopping", 1710000041000),
        Transaction(43, 800.0, "INCOME", "Salary Bonus", 1710000042000),
        Transaction(44, -60.0, "EXPENSE", "Taxi", 1710000043000),
        Transaction(45, -20.0, "EXPENSE", "Snacks", 1710000044000),
        Transaction(46, 350.0, "INCOME", "Part-time", 1710000045000),
        Transaction(47, -75.0, "EXPENSE", "Fuel", 1710000046000),
        Transaction(48, -200.0, "EXPENSE", "Bills", 1710000047000),
        Transaction(49, -30.0, "EXPENSE", "Lunch", 1710000048000),
        Transaction(50, 150.0, "INCOME", "Gift", 1710000049000),
        Transaction(51, -55.0, "EXPENSE", "Breakfast", 1710000050000),
        Transaction(52, -120.0, "EXPENSE", "Lunch", 1710000051000),
        Transaction(53, -250.0, "EXPENSE", "Dinner", 1710000052000),
        Transaction(54, 2000.0, "INCOME", "Salary", 1710000053000),
        Transaction(55, -300.0, "EXPENSE", "Shopping", 1710000054000),
        Transaction(56, -90.0, "EXPENSE", "Taxi", 1710000055000),
        Transaction(57, 450.0, "INCOME", "Freelance", 1710000056000),
        Transaction(58, -60.0, "EXPENSE", "Coffee", 1710000057000),
        Transaction(59, -700.0, "EXPENSE", "Rent", 1710000058000),
        Transaction(60, 200.0, "INCOME", "Gift", 1710000059000),

        Transaction(61, -80.0, "EXPENSE", "Groceries", 1710000060000),
        Transaction(62, -40.0, "EXPENSE", "Snacks", 1710000061000),
        Transaction(63, 300.0, "INCOME", "Bonus", 1710000062000),
        Transaction(64, -180.0, "EXPENSE", "Clothes", 1710000063000),
        Transaction(65, -25.0, "EXPENSE", "Parking", 1710000064000),
        Transaction(66, 500.0, "INCOME", "Side Job", 1710000065000),
        Transaction(67, -100.0, "EXPENSE", "Fuel", 1710000066000),
        Transaction(68, -70.0, "EXPENSE", "Cinema", 1710000067000),
        Transaction(69, -20.0, "EXPENSE", "Water", 1710000068000),
        Transaction(70, 350.0, "INCOME", "Commission", 1710000069000),

        Transaction(71, -400.0, "EXPENSE", "Hotel", 1710000070000),
        Transaction(72, -60.0, "EXPENSE", "Internet", 1710000071000),
        Transaction(73, 900.0, "INCOME", "Project", 1710000072000),
        Transaction(74, -50.0, "EXPENSE", "Laundry", 1710000073000),
        Transaction(75, -30.0, "EXPENSE", "Milk Tea", 1710000074000),
        Transaction(76, 150.0, "INCOME", "Refund", 1710000075000),
        Transaction(77, -220.0, "EXPENSE", "Electronics", 1710000076000),
        Transaction(78, -45.0, "EXPENSE", "Breakfast", 1710000077000),
        Transaction(79, -110.0, "EXPENSE", "Dinner", 1710000078000),
        Transaction(80, 600.0, "INCOME", "Freelance", 1710000079000),

        Transaction(81, -60.0, "EXPENSE", "Coffee", 1710000080000),
        Transaction(82, -150.0, "EXPENSE", "Shopping", 1710000081000),
        Transaction(83, 1000.0, "INCOME", "Salary Bonus", 1710000082000),
        Transaction(84, -80.0, "EXPENSE", "Taxi", 1710000083000),
        Transaction(85, -25.0, "EXPENSE", "Snacks", 1710000084000),
        Transaction(86, 400.0, "INCOME", "Part-time", 1710000085000),
        Transaction(87, -95.0, "EXPENSE", "Fuel", 1710000086000),
        Transaction(88, -300.0, "EXPENSE", "Bills", 1710000087000),
        Transaction(89, -50.0, "EXPENSE", "Lunch", 1710000088000),
        Transaction(90, 180.0, "INCOME", "Gift", 1710000089000),

        Transaction(91, -70.0, "EXPENSE", "Groceries", 1710000090000),
        Transaction(92, -35.0, "EXPENSE", "Snacks", 1710000091000),
        Transaction(93, 220.0, "INCOME", "Bonus", 1710000092000),
        Transaction(94, -140.0, "EXPENSE", "Clothes", 1710000093000),
        Transaction(95, -30.0, "EXPENSE", "Parking", 1710000094000),
        Transaction(96, 480.0, "INCOME", "Side Job", 1710000095000),
        Transaction(97, -85.0, "EXPENSE", "Fuel", 1710000096000),
        Transaction(98, -65.0, "EXPENSE", "Cinema", 1710000097000),
        Transaction(99, -18.0, "EXPENSE", "Water", 1710000098000),
        Transaction(100, 300.0, "INCOME", "Commission", 1710000099000),
        Transaction(101, -45.0, "EXPENSE", "Breakfast", 1710000100000),
        Transaction(102, -110.0, "EXPENSE", "Lunch", 1710000101000),
        Transaction(103, -200.0, "EXPENSE", "Dinner", 1710000102000),
        Transaction(104, 1800.0, "INCOME", "Salary", 1710000103000),
        Transaction(105, -250.0, "EXPENSE", "Shopping", 1710000104000),
        Transaction(106, -70.0, "EXPENSE", "Taxi", 1710000105000),
        Transaction(107, 500.0, "INCOME", "Freelance", 1710000106000),
        Transaction(108, -55.0, "EXPENSE", "Coffee", 1710000107000),
        Transaction(109, -650.0, "EXPENSE", "Rent", 1710000108000),
        Transaction(110, 220.0, "INCOME", "Gift", 1710000109000),

        Transaction(111, -90.0, "EXPENSE", "Groceries", 1710000110000),
        Transaction(112, -35.0, "EXPENSE", "Snacks", 1710000111000),
        Transaction(113, 260.0, "INCOME", "Bonus", 1710000112000),
        Transaction(114, -170.0, "EXPENSE", "Clothes", 1710000113000),
        Transaction(115, -20.0, "EXPENSE", "Parking", 1710000114000),
        Transaction(116, 420.0, "INCOME", "Side Job", 1710000115000),
        Transaction(117, -95.0, "EXPENSE", "Fuel", 1710000116000),
        Transaction(118, -75.0, "EXPENSE", "Cinema", 1710000117000),
        Transaction(119, -22.0, "EXPENSE", "Water", 1710000118000),
        Transaction(120, 310.0, "INCOME", "Commission", 1710000119000),

        Transaction(121, -350.0, "EXPENSE", "Hotel", 1710000120000),
        Transaction(122, -55.0, "EXPENSE", "Internet", 1710000121000),
        Transaction(123, 880.0, "INCOME", "Project", 1710000122000),
        Transaction(124, -60.0, "EXPENSE", "Laundry", 1710000123000),
        Transaction(125, -28.0, "EXPENSE", "Milk Tea", 1710000124000),
        Transaction(126, 140.0, "INCOME", "Refund", 1710000125000),
        Transaction(127, -210.0, "EXPENSE", "Electronics", 1710000126000),
        Transaction(128, -50.0, "EXPENSE", "Breakfast", 1710000127000),
        Transaction(129, -100.0, "EXPENSE", "Dinner", 1710000128000),
        Transaction(130, 550.0, "INCOME", "Freelance", 1710000129000),

        Transaction(131, -65.0, "EXPENSE", "Coffee", 1710000130000),
        Transaction(132, -130.0, "EXPENSE", "Shopping", 1710000131000),
        Transaction(133, 950.0, "INCOME", "Salary Bonus", 1710000132000),
        Transaction(134, -75.0, "EXPENSE", "Taxi", 1710000133000),
        Transaction(135, -30.0, "EXPENSE", "Snacks", 1710000134000),
        Transaction(136, 390.0, "INCOME", "Part-time", 1710000135000),
        Transaction(137, -88.0, "EXPENSE", "Fuel", 1710000136000),
        Transaction(138, -280.0, "EXPENSE", "Bills", 1710000137000),
        Transaction(139, -45.0, "EXPENSE", "Lunch", 1710000138000),
        Transaction(140, 170.0, "INCOME", "Gift", 1710000139000),

        Transaction(141, -75.0, "EXPENSE", "Groceries", 1710000140000),
        Transaction(142, -40.0, "EXPENSE", "Snacks", 1710000141000),
        Transaction(143, 240.0, "INCOME", "Bonus", 1710000142000),
        Transaction(144, -150.0, "EXPENSE", "Clothes", 1710000143000),
        Transaction(145, -25.0, "EXPENSE", "Parking", 1710000144000),
        Transaction(146, 460.0, "INCOME", "Side Job", 1710000145000),
        Transaction(147, -92.0, "EXPENSE", "Fuel", 1710000146000),
        Transaction(148, -68.0, "EXPENSE", "Cinema", 1710000147000),
        Transaction(149, -19.0, "EXPENSE", "Water", 1710000148000),
        Transaction(150, 330.0, "INCOME", "Commission", 1710000149000),
        Transaction(151, -60.0, "EXPENSE", "Breakfast", 1710000150000),
        Transaction(152, -130.0, "EXPENSE", "Lunch", 1710000151000),
        Transaction(153, -220.0, "EXPENSE", "Dinner", 1710000152000),
        Transaction(154, 2100.0, "INCOME", "Salary", 1710000153000),
        Transaction(155, -280.0, "EXPENSE", "Shopping", 1710000154000),
        Transaction(156, -85.0, "EXPENSE", "Taxi", 1710000155000),
        Transaction(157, 520.0, "INCOME", "Freelance", 1710000156000),
        Transaction(158, -65.0, "EXPENSE", "Coffee", 1710000157000),
        Transaction(159, -750.0, "EXPENSE", "Rent", 1710000158000),
        Transaction(160, 240.0, "INCOME", "Gift", 1710000159000),

        Transaction(161, -95.0, "EXPENSE", "Groceries", 1710000160000),
        Transaction(162, -45.0, "EXPENSE", "Snacks", 1710000161000),
        Transaction(163, 280.0, "INCOME", "Bonus", 1710000162000),
        Transaction(164, -190.0, "EXPENSE", "Clothes", 1710000163000),
        Transaction(165, -30.0, "EXPENSE", "Parking", 1710000164000),
        Transaction(166, 460.0, "INCOME", "Side Job", 1710000165000),
        Transaction(167, -105.0, "EXPENSE", "Fuel", 1710000166000),
        Transaction(168, -80.0, "EXPENSE", "Cinema", 1710000167000),
        Transaction(169, -25.0, "EXPENSE", "Water", 1710000168000),
        Transaction(170, 360.0, "INCOME", "Commission", 1710000169000),

        Transaction(171, -420.0, "EXPENSE", "Hotel", 1710000170000),
        Transaction(172, -70.0, "EXPENSE", "Internet", 1710000171000),
        Transaction(173, 920.0, "INCOME", "Project", 1710000172000),
        Transaction(174, -65.0, "EXPENSE", "Laundry", 1710000173000),
        Transaction(175, -35.0, "EXPENSE", "Milk Tea", 1710000174000),
        Transaction(176, 160.0, "INCOME", "Refund", 1710000175000),
        Transaction(177, -230.0, "EXPENSE", "Electronics", 1710000176000),
        Transaction(178, -55.0, "EXPENSE", "Breakfast", 1710000177000),
        Transaction(179, -120.0, "EXPENSE", "Dinner", 1710000178000),
        Transaction(180, 620.0, "INCOME", "Freelance", 1710000179000),

        Transaction(181, -70.0, "EXPENSE", "Coffee", 1710000180000),
        Transaction(182, -160.0, "EXPENSE", "Shopping", 1710000181000),
        Transaction(183, 1100.0, "INCOME", "Salary Bonus", 1710000182000),
        Transaction(184, -90.0, "EXPENSE", "Taxi", 1710000183000),
        Transaction(185, -35.0, "EXPENSE", "Snacks", 1710000184000),
        Transaction(186, 420.0, "INCOME", "Part-time", 1710000185000),
        Transaction(187, -100.0, "EXPENSE", "Fuel", 1710000186000),
        Transaction(188, -320.0, "EXPENSE", "Bills", 1710000187000),
        Transaction(189, -55.0, "EXPENSE", "Lunch", 1710000188000),
        Transaction(190, 200.0, "INCOME", "Gift", 1710000189000),

        Transaction(191, -85.0, "EXPENSE", "Groceries", 1710000190000),
        Transaction(192, -50.0, "EXPENSE", "Snacks", 1710000191000),
        Transaction(193, 260.0, "INCOME", "Bonus", 1710000192000),
        Transaction(194, -170.0, "EXPENSE", "Clothes", 1710000193000),
        Transaction(195, -35.0, "EXPENSE", "Parking", 1710000194000),
        Transaction(196, 500.0, "INCOME", "Side Job", 1710000195000),
        Transaction(197, -110.0, "EXPENSE", "Fuel", 1710000196000),
        Transaction(198, -75.0, "EXPENSE", "Cinema", 1710000197000),
        Transaction(199, -28.0, "EXPENSE", "Water", 1710000198000),
        Transaction(200, 380.0, "INCOME", "Commission", 1710000199000),
    )

    val smsList = listOf(
        SmsMessage("Bank", "You received 200k", System.currentTimeMillis()),
        SmsMessage("Bank", "Payment of 50k completed", System.currentTimeMillis()),
        SmsMessage("Shop", "You spent 150k", System.currentTimeMillis())
    )
}

// ===================== EXTENSION =====================
fun List<Transaction>.totalIncome(): Double {
    return filter { it.amount > 0 }.sumOf { it.amount }
}

fun List<Transaction>.totalExpense(): Double {
    return filter { it.amount < 0 }.sumOf { it.amount }
}

// ===================== LOGGER =====================
object Logger {
    fun log(message: String) {
        println("LOG: $message")
    }
}

// ===================== SIMULATOR =====================
class AppSimulator {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}

class AppSimulator1 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator3 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator4 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator5 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator6 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator8 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator9 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator10 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator11 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator12 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator15 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")






























































































































    }
}
class AppSimulator17 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator18 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()






















        Logger.log("Total: ${data.totalIncome()}")
    }
}


class AppSimulator20 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()











        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator21 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator23 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator34{

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator35 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}
class AppSimulator99 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}












class AppSimulator90 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}




class AppSimulator89 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}









class AppSimulator00 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}










class AppSimulator55 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}








class AppSimulator44 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}




class AppSimulator88 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}







class AppSimulator56 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}












class AppSimulator69 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}

class AppSimulator07 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}












class AppSimulator08 {

    suspend fun run() {
        val dao = FakeTransactionDao()
        val api = FakeApiService()
        val repo = TransactionRepository(dao, api)

        val useCase = RefreshTransactionsUseCase(repo)

        val data = useCase()

        Logger.log("Total: ${data.totalIncome()}")
    }
}





















