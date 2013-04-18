Compiler.Control.Print.signatures := 0;
Compiler.Control.Print.printOpens := false;
SMLofNJ.Internals.GC.messages false;
CM.autoloading (SOME true);
CM.autoload' "/usr/local/share/rsltc/sml/rslml.cm";
use "COUNTER_.sml" handle RSL.RSL_exception s => Compiler.Control.Print.say (s ^ "\n") | RSL.swap s => Compiler.Control.Print.say (s ^ "\n");

