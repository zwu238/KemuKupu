(voice_akl_mi_pk06_cg)
(Parameter.set 'Duration_Stretch 
1.0
) 
(set! after_synth_hooks (list (lambda (utt) (utt.wave.rescale utt 
2.0
 t))))
(SayText "
kahumoe
"))
