structure RT_Nat = RT_Nat;

structure COUNTER =
    struct
        ;
        
        val counter_: RT_Nat.t ref = ref (RT_Int.fromLit "0");
    end;
    
open COUNTER;

RSL.print_load_errs();
RSL.set_time();
R_coverage.init();
();
RSL.print_error_count();
R_coverage.save_marked();
