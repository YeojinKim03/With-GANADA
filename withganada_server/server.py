#!/usr/bin/python3
# -*- coding: utf-8 -*-

import os
import socket
import time
import keras
import librosa
from keras.models import load_model
from tensorflow.keras.preprocessing import sequence
import numpy as np
model_A = load_model('ganada_A_220908.h5') # 1단계 model
model_B = load_model('ganada_B_220908_acc97.h5') # 2단계 model
model_C = load_model('ganada_C_220908_acc99.h5') # 3단계 model

"""# 앱에서 단계, 레이블, 녹음 파일(mp3) 전달받으면
 - 레이블 index 결정
 - 각 단계에 해당하는 모델을 따로 사용
"""

file_path = '/var/www/html/upload/recorded.mp3' # 전달받은 파일 path

host = '10.0.2.15' # 호스트 ip
port = 12344            # 포트번호

server_sock = socket.socket(socket.AF_INET)
server_sock.bind((host, port))
server_sock.listen(1)
print("기다리는 중..")
out_data = int(10)

#####################################


#####################################



while True: #안드로이드에서 연결 버튼 누를 때까지 기다린다.
    print("re 기다리는 중..")
    client_sock, addr = server_sock.accept() # 연결 승인

    if client_sock: #client_sock 가 null 값이 아니라면 (연결 승인 되었다면)
        print('Connected by?!', addr) #연결주소 print
        in_data = client_sock.recv(1024) #안드로이드에서 "refresh" 전송
        print('rcv :', in_data.decode("utf-8"), len(in_data)) #전송 받은값 디코딩
        stagenum = in_data.decode("utf-8")
        time.sleep(1)
        checkfile = 0
        while checkfile:
         if os.path.isfile(file_path):
          break
         elif checkfile >= 3 :
          client_sock.send("nofile".encode("utf-8"))
          break
         else : 
          checkfile = checkfile + 1
          time.sleep(1)
        SAMPLING_RATE = 22050
        test_x = []
        test_data, _ = librosa.load(file_path, sr=SAMPLING_RATE)
        test_x.append(test_data)
        padded_test_x = sequence.pad_sequences(test_x, maxlen=22050, padding='post', dtype=float)
        len(padded_test_x[0]) # 테스트용 코드
        test_x = np.array(padded_test_x)

        if in_data == b'\x00\x010':
            y_ = model_A.predict(test_x)
            print('model A :', stagenum)
        elif in_data == b'\x00\x011':
            y_ = model_B.predict(test_x)
            print('model B :', stagenum)
        elif in_data == b'\x00\x012':
            y_ = model_C.predict(test_x)
            print('model C :', stagenum)
        else:
            y_ = model_A.predict(test_x)
            print('model AAA :', stagenum)

        predicted = np.argmax(y_, axis=-1)

        acc = y_[0]		#result
        result_y = predicted[0]		#result
        #print("y_: "+ str(y_))
        print("result_y: "+ str(result_y))
        print("acc_0: "+ str(acc[0])+"  acc_1: "+ str(acc[1])+"  acc_2: "+ str(acc[2]))
        acc0str = str(round(acc[0]*100))
        acc1str = str(round(acc[1]*100))
        acc2str = str(round(acc[2]*100))
        print("acc_0: "+ acc0str + "  acc_1: " + acc1str + "  acc_2: "+ acc2str)
        # 결과 return 하여 표시하도록 함
        finalresult = str(result_y) + "," + acc0str + "," + acc1str + "," + acc2str 
        print("final = " + finalresult)
        client_sock.send(finalresult.encode("utf-8")) 
        time.sleep(2)  
        client_sock.send("stop".encode("utf-8"))
        if os.path.isfile(file_path):
         os.remove(file_path)


client_sock.close()
server_sock.close()
