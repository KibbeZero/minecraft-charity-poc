package com.kibbezero.extralife.playercapability;

import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("ConstantConditions") //Suppressed because @CapabilityInject above takes care of instantiating DONOR_DRIVE_TAG_CAPABILITY
public class DonorDriveTagCapability implements ICapabilitySerializable<StringNBT> {

    @CapabilityInject(IDonorDriveTag.class)
    public static final Capability<IDonorDriveTag> DONOR_DRIVE_TAG_CAPABILITY = null;
    @SuppressWarnings("NullableProblems") //Suppressed because @CapabilityInject above takes care of instantiating DONOR_DRIVE_TAG_CAPABILITY
    private final LazyOptional<IDonorDriveTag> instance = LazyOptional.of(DONOR_DRIVE_TAG_CAPABILITY::getDefaultInstance);

    public static void register() {
        CapabilityManager.INSTANCE.register(IDonorDriveTag.class, new DonorDriveStorage(), DonorDriveTag::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        return cap == DONOR_DRIVE_TAG_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }


    @Override
    public StringNBT serializeNBT() {
        return (StringNBT) DONOR_DRIVE_TAG_CAPABILITY.getStorage().writeNBT(DONOR_DRIVE_TAG_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(StringNBT nbt) {
        DONOR_DRIVE_TAG_CAPABILITY.getStorage().readNBT(DONOR_DRIVE_TAG_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }
}
