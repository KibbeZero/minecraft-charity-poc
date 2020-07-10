package com.kibbezero.extralife.playercapability;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class DonorDriveStorage implements Capability.IStorage<IDonorDriveTag> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IDonorDriveTag> capability, IDonorDriveTag instance, Direction side) {
        return StringNBT.valueOf(instance.getDonorDriveId());
    }

    @Override
    public void readNBT(final Capability<IDonorDriveTag> capability, final IDonorDriveTag instance, final Direction side, final INBT nbt) {

        if(!(instance instanceof DonorDriveTag)){
            throw new IllegalArgumentException("Cannot deserialize to an instance of DonorDriveTag.");
        }

        instance.setPlayerAssociation((nbt).getString());
    }
}
