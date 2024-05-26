from flask import Flask, Response
import pyautogui
import io
from PIL import Image

app = Flask(__name__)

@app.route('/screen')
def stream_screen():

    screen = pyautogui.screenshot() # Ekran görüntüsünü yakalama

    # Ekran görüntüsünü byte dizisine dönüştürme
    img_io = io.BytesIO()
    screen.save(img_io, 'JPEG')
    img_io.seek(0)


    return Response(img_io, mimetype='image/jpeg') # Ekran görüntüsünü yanıt olarak gönderme


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
