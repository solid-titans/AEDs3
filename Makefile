runner      = button.sh
cleaner     = limparAmbiente.sh
safeCleaner = limparJava.sh

run:
	sh $(runner)

clean:
	sh $(cleaner)

safeClean:
	sh $(safeCleaner)
