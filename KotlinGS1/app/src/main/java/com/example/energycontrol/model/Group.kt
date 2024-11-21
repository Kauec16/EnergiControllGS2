package com.example.energycontrol.model

class Group {

    public var id: Int? = null;
    public var name: String = "";
    public var frequency: String = ""


    constructor(name: String, frequency: String) {
        this.id = null
        this.name = name
        this.frequency = frequency
    }


    constructor(id: Int, name: String, frequency: String) {
        this.id = id
        this.name = name
        this.frequency = frequency
    }
}