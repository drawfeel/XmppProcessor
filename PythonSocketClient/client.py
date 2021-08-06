import socket

class Client():
    def send_msg(self):
        LOCAL_PORT=7779
        goon=True
        try:
            socket.setdefaulttimeout(5)
            s=socket.socket()
            s.connect(("localhost", LOCAL_PORT))
            print("input data ('EXIT' for shutting server down)")
            while(goon):
                msg = input(">>>").strip()
                if len(msg)==0 :
                    continue
                s.send(msg.encode("utf-8"))
                s.send("\r\n".encode("utf-8"))

                f = open('./json.txt', 'rb')
                s.send(f.read())
                s.send("\r\n".encode("utf-8"))

#                data=s.recv(256)
#                if len(data) > 0 :
#                    print("recv:>", data.decode("utf-8"))
                if "EXIT" == msg:
                    goon=False;
                    print("BYE ...")
                    s.close()
        except ConnectionError as ex:
            print(ex)

if __name__ == "__main__":
    client=Client()
    client.send_msg()
