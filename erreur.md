# Si on rencontre cette erreur
     Caused by: org.hibernate.StaleObjectStateException: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [emcer.cg.fr.gestionutilisateuronline.wizard.Wizard#1]
      at org.hibernate.event.internal.DefaultMergeEventListener.entityIsDetached(DefaultMergeEventListener.java:426) ~[hibernate-core-6.6.2.Final.jar:6.6.2.Final]
      at org.hibernate.event.internal.DefaultMergeEventListener.merge(DefaultMergeEventListener.java:214) ~[hibernate-core-6.6.2.Final.jar:6.6.2.Final]
      at org.hibernate.event.internal.DefaultMergeEventListener.doMerge(DefaultMergeEventListener.java:152) ~[hibernate-core-6.6.2.Final.jar:6.6.2.Final]
      at org.hibernate.event.internal.DefaultMergeEventListener.onMerge(DefaultMergeEventListener.java:136) ~[hibernate-core-6.6.2.Final.jar:6.6.2.Final]
      at org.hibernate.event.internal.DefaultMergeEventListener.onMerge(DefaultMergeEventListener.java:89) ~[hibernate-core-6.6.2.Final.jar:6.6.2.Final]
      at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:127) ~[hibernate-core-6.6.2.Final.jar:6.6.2.Final]
      at org.hibernate.internal.SessionImpl.fireMerge(SessionImpl.java:854) ~[hibernate-core-6.6.2.Final.jar:6.6.2.Final]
      at org.hibernate.internal.SessionImpl.merge(SessionImpl.java:840) ~[hibernate-core-6.6.2.Final.jar:6.6.2.Final]
      at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
      at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
    
    NB: 
    c'est parce quon a voulu crée des instances avec des ids ipmlicite et la 
    sauvegarder or les id sont auto incrementer. donc il fdaut retirer les id(a4.setId(4L);)
        Artifact a4 = new Artifact();
        a4.setId(4L);
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl4");
