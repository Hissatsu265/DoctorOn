package com.example.doctoron.Objects
import android.content.Context
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.nio.charset.StandardCharsets

class MQTT(context: Context, serverUri: String, clientId: String) {

    private val mqttAndroidClient: MqttAndroidClient = MqttAndroidClient(context, serverUri, clientId)
    private val mqttConnectOptions: MqttConnectOptions = MqttConnectOptions()

    init {
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                // Khi kết nối thành công với MQTT server
            }

            override fun connectionLost(cause: Throwable?) {
                // Khi kết nối bị mất
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                // Khi nhận được tin nhắn từ MQTT server
                val payload = message.payload.toString(StandardCharsets.UTF_8)
                // Xử lý dữ liệu ở đây
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // Khi một tin nhắn đã được gửi thành công
            }
        })

        // Thiết lập các tuỳ chọn kết nối MQTT (nếu cần)
        // mqttConnectOptions.isAutomaticReconnect = true
        // mqttConnectOptions.isCleanSession = false

        connect()
    }

    private fun connect() {
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    // Khi kết nối thành công
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    // Khi kết nối thất bại
                }
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun disconnect() {
        mqttAndroidClient.disconnect()
    }

    fun subscribeToTopic(topic: String, qos: Int) {
        mqttAndroidClient.subscribe(topic, qos)
    }

    fun publishMessage(topic: String, payload: String, qos: Int, retained: Boolean) {
        val message = MqttMessage(payload.toByteArray(StandardCharsets.UTF_8))
        message.qos = qos
        message.isRetained = retained
        mqttAndroidClient.publish(topic, message)
    }
}
