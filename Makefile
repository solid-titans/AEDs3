runner      = button.sh
cleaner     = limparAmbiente.sh
safeCleaner = limparJava.sh
compiler    = compile.sh
makeJar     = makeJar.sh

run:
	sh $(runner)

clean:
	sh $(cleaner)

safeClean:
	sh $(safeCleaner)

compile:
	sh $(compiler)

jar: compile
	sh $(makeJar)
