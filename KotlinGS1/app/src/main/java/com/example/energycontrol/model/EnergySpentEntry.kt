package com.example.energycontrol.model

class EnergySpentEntry {


    public var id = 0;
    public var groupId = 0;
    public var date = "";
    public var value = 0.0;
    public var frequency = "";
    public var groupName = "";

    constructor(groupId: Int, date: String, value: Double) {
        this.groupId = groupId
        this.date = date
        this.value = value
    }

    constructor(
        id: Int,
        groupId: Int,
        groupName: String,
        date: String,
        frequency: String,
        value: Double,
    ) {
        this.id = id
        this.groupId = groupId
        this.date = date
        this.value = value
        this.frequency = frequency;
        this.groupName = groupName
    }


}