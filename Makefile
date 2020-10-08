runner = button.sh
cleaner = limparAmbiente.sh
path = $(shell pwd)/src

all:
	$(cd $(path))
	javac -d $(path) Main.java
	
clean:
	sh $(cleaner)
	
run:
	sh $(runner)
