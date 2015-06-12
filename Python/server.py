import socket
import re

# Initialize a socket object
s = socket.socket()
# Get the machine IP
host = '0.0.0.0'
# Reserve a port for your service and bind a port. 
port = 10002              	
s.bind((host, port))        
# listening for clients connection.
s.listen(5)
quit = False                 
while not quit:
# Opening connection with client.
	c, addr = s.accept()     
	while True:
		print ('Got connection from', addr)
		c.send('> ')
		msg = c.recv(1024)
		if not msg:
			c.close
			break
	   
	    #Receving command 'quit' will disconnect client
		if 'quit' in msg:
	   		c.send('Goodbye \n')
	   		c.close()
	   		break
	   	elif 'Quit' in msg:
			c.send('Goodbye \n')
	   		c.close()
	   		s.close()
	   		quit = True
	   		break

		#Regular expression for matching numbers and operator
		result = re.match("(\d+\.?\d*)\s*(\+|\*|\-|/)\s*(\d+\.?\d*)",msg)

		if(result):
			x = float(result.group(1))
			y = float(result.group(3))
			operator = result.group(2)
			if(operator == "+"):
				c.send(str(x + y)+ "\n")
			elif (operator == "*"):
				c.send(str(x * y)+ "\n")
			elif (operator == '/'):
				c.send(str(x / y)+ "\n")
			elif (operator == '-'):
				c.send(str(x - y) + "\n")
		else:
			c.send('wrong expression\n')
