runner = button.sh
cleaner = limparAmbiente.sh
path = $(shell pwd)/src

run:
	sh $(runner)

clean:
	sh $(cleaner)
