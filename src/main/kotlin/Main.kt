/* Домашнее задание 1
Написать программу, которая обрабатывает введённые пользователем в консоль команды:
exit
help
add <Имя> phone <Номер телефона>
add <Имя> email <Адрес электронной почты>

После выполнения команды, кроме команды exit, программа ждёт следующую команду.

Имя – любое слово.
Если введена команда с номером телефона, нужно проверить, что указанный телефон может начинаться с +, затем идут только цифры.
При соответствии введённого номера этому условию – выводим его на экран вместе с именем, используя строковый шаблон.
В противном случае - выводим сообщение об ошибке.
Для команды с электронной почтой делаем то же самое, но с другим шаблоном – для простоты, адрес должен содержать три
последовательности букв, разделённых символами @ и точкой.

Пример команд:
add Tom email tom@example.com
add Tom phone +7876743558
 */

/*Домашнее задание 2
За основу берём код решения домашнего задания из предыдущего семинара и дорабатываем его.

— Создайте иерархию sealed классов, которые представляют собой команды. В корне иерархии интерфейс Command.
— В каждом классе иерархии должна быть функция isValid(): Boolean, которая возвращает true, если команда
введена с корректными аргументами. Проверку телефона и email нужно перенести в эту функцию.
— Напишите функцию readCommand(): Command, которая читает команду из текстового ввода, распознаёт её и
возвращает один из классов-наследников Command, соответствующий введённой команде.
— Создайте data класс Person, который представляет собой запись о человеке. Этот класс должен содержать поля:
name – имя человека
phone – номер телефона
email – адрес электронной почты
— Добавьте новую команду show, которая выводит последнее значение, введённой с помощью команды add.
Для этого значение должно быть сохранено в переменную типа Person. Если на момент выполнения команды show не
было ничего введено, нужно вывести на экран сообщение “Not initialized”.
— Функция main должна выглядеть следующем образом. Для каждой команды от пользователя:
Читаем команду с помощью функции readCommand
Выводим на экран получившийся экземпляр Command
Если isValid для команды возвращает false, выводим help. Если true, обрабатываем команду внутри when.
 */
lateinit var n: String
var phoneContact: Map<String, String> = mapOf()
var mailContact: Map<String, String> = mapOf()
var flag: Boolean = true
sealed interface Command {
    fun isValid(): Boolean
}

data class AddPhoneCommand(val name: String, val phone: String) : Command {
    override fun isValid() = phone.matches(Regex("""^\+?\d+${'$'}"""))
}


data class AddEmailCommand(val name: String, val email: String) : Command {
    override fun isValid() = email.matches(Regex("""^[A-Za-z\d](.*)(@)(.+)(\.)([A-Za-z]{2,})"""))
}

object ExitCommand : Command {
    override fun isValid() = true
}

object HelpCommand : Command {
    override fun isValid() = true
}

object ShowCommand : Command {
    override fun isValid() = true
}

// Класс Person
data class Person(var name: String, var phone: String? = null, var email: String? = null){
    override fun toString(): String {
        return "Name: ${name}, Phone: ${phone}, Email: ${email}"
    }
}

fun readCommand(input: String): Command {
    val parts = input.split(" ")
    // Распознавание команды
    return when (parts[0]) {
        "add" -> {
            if (parts.size == 4) {
                when (parts[2]) {
                    "phone" -> AddPhoneCommand(parts[1], parts[3])
                    "email" -> AddEmailCommand(parts[1], parts[3])
                    else -> HelpCommand
                }
            } else {
                return HelpCommand
            }
        }

        "exit" -> ExitCommand
        "help" -> HelpCommand
        "show" -> ShowCommand
        else -> {
            println("Неизвестная команда")
            HelpCommand
        }
    }
}

fun main(){
    println("""Добро пожаловать в записную книжку
    Список команд:
    exit - выход
    help - справка
    add <Имя> phone <Номер телефона> - Добавить имя человека и номер телефона(только цифры(может начинаться с +)
    add <Имя> email <Адрес электронной почты> - Добавить имя человека и mail(обязательно в адресе содержит @ и .)
    show - показать последнюю добавленную запись""")
    var person: Person? = null
    while (true) {
        print("Введите команду: ")
        val command = readCommand(readLine()!!)
        if (command.isValid()) {
            when (command) {
                is AddPhoneCommand -> {
                    person = Person(command.name, phone = command.phone)
                    println("Добавлено: Name: ${person!!.name}, телефон: ${person!!.phone}")
                }

                is AddEmailCommand -> {
                    person = Person(command.name, email = command.email)
                    println("Добавлено: Name: ${person!!.name}, email: ${person!!.email}")
                }

                is ShowCommand -> {
                    if (person == null) {
                        println("Not initialized")
                    } else {
                        println("Последняя запись: $person")
                    }
                }

                is HelpCommand -> {
                    println("""Список команд:
    exit - выход
    help - справка
    add <Имя> phone <Номер телефона> - Добавить имя человека и номер телефона(только цифры(может начинаться с +)
    add <Имя> email <Адрес электронной почты> - Добавить имя человека и mail(обязательно в адресе содержит @ и .)
    show - показать последнюю добавленную запись""")
                }
                is ExitCommand -> return
            }
        }
    }
}
/*
fun main1() { //старый код
    println("""Добро пожаловать в записную книжку
    Список команд:
    exit - выход
    help - справка
    add - добавить контакт
    show - показать последнюю добавленную запись""")
    while(true){
        println("Введите команду")
        n = readlnOrNull().toString()
        when (n) {
            "exit" -> {
                flag = false
            }
            "help" -> {
                println("""Список команд:
    |exit - выход
    |help - справка
    |add - добавить контакт
    |showPhone - список телефонов
    |showEmail - список почтовы адресов)""")
            }
            "add" -> {
                println("""Способ ввода:
    |<Имя> phone <Номер телефона> - Добавить имя человека и номер телефона(только цифры(может начинаться с +)
    |<Имя> email <Адрес электронной почты> - Добавить имя человека и mail(обязательно в адресе содержит @ и .)""")
                n = readlnOrNull().toString()
                addContactPhoneOrMail(n)
            }
            "showPhone" -> {
                println(phoneContact)
            }

            "showEmail" -> {
                println(mailContact)
            }
            else -> {
                println("Некорректный пункт меню! Введите help, для ознакомления с командами")
            }
        }
    }
}
fun addContactPhoneOrMail(n: String) {
    if (n.filter { it == ' ' }.count() == 2) {
        val words = n.split("\\s".toRegex()).toTypedArray()
        if (words[1] == "phone") {
            if (words[2].contains(Regex("""[0-9]+"""))) {
                if ((words[2][0]).toString().contains("""[0-9]""".toRegex()) || (words[2][0]).toString().contains("[+]".toRegex())) {
                    phoneContact += mapOf(words[0] to words[2])
                    println("Контакт добавлен")
                } else {
                    println("Данные введены некорректно. Введите help -> add для уточнения параметров")
                }
            } else {
                println("Данные введены некорректно. Введите help -> add для уточнения параметров")
            }
        } else if (words[1] == "email") {
            if (words[2].contains("""@""".toRegex()) && words[2].contains(""".""".toRegex())) {
                mailContact += mapOf(words[0] to words[2])
                println("Контакт добавлен")
            } else {
                println("Данные введены некорректно. Введите help -> add для уточнения параметров")
            }
        } else {
            println("Данные введены некорректно. Введите help -> add для уточнения параметров")
        }
    } else {
        println("Данные введены некорректно. Введите help -> add для уточнения параметров")
    }
}

 */
