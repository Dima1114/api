package api.jms

interface JmsSender<T> {

    fun sendMessage(template: T, vararg resources: Any)
}