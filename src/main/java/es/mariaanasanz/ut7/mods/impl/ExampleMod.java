package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(DamMod.MOD_ID)
public class ExampleMod extends DamMod implements IBlockBreakEvent, IServerStartEvent,
        IItemPickupEvent, ILivingDamageEvent, IUseItemEvent, IFishedEvent,
        IInteractEvent, IMovementEvent {

    private int cuantosItems;
    MobEffectInstance efecto1;
    MobEffectInstance efecto2;

    public ExampleMod(){
        super();
    }

    @Override
    public String autor() {
        return "Javier Jorge Soteras";
    }

    @Override
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        System.out.println("Bloque destruido en la posicion "+pos);
    }

    @Override
    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        LOGGER.info("Server starting");
    }

    @Override
    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        LOGGER.info("Item recogido");
        System.out.println("Item recogido");
    }

    @Override
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        System.out.println("evento LivingDamageEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());
    }

    @Override
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        System.out.println("evento LivingDeathEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());

    }

    @Override
    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LOGGER.info("evento LivingEntityUseItemEvent invocado "+event.getEntity().getClass());
    }


    @Override
    @SubscribeEvent
    public void onPlayerFish(ItemFishedEvent event) {
        System.out.println("¡Has pescado un pez!");
    }

    @Override
    @SubscribeEvent
    public void onPlayerTouch(PlayerInteractEvent.RightClickBlock event) {
        System.out.println("¡Has hecho click derecho!");
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        if (ItemStack.EMPTY.equals(heldItem)) {
            System.out.println("La mano esta vacia");
            if (state.getBlock().getName().getString().trim().toLowerCase().endsWith("log")) {
                System.out.println("¡Has hecho click sobre un tronco!");
            }
        }
    }

    @Override
    @SubscribeEvent
    public void onPlayerWalk(MovementInputUpdateEvent event) {
        if(event.getEntity() instanceof Player){
            if(event.getInput().down){
                System.out.println("down"+event.getInput().down);
            }
            if(event.getInput().up){
                System.out.println("up"+event.getInput().up);
            }
            if(event.getInput().right){
                System.out.println("right"+event.getInput().right);
            }
            if(event.getInput().left){
                System.out.println("left"+event.getInput().left);
            }
        }

        Player player = event.getEntity();

        player.removeEffect(efecto1.getEffect());
        player.removeEffect(efecto2.getEffect());
        for (ItemStack objeto : player.getInventory().items) {
            if(objeto.getCount() != 0) {
                cuantosItems++;
            }
        }
        if (cuantosItems <= 9) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 2));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 999999, 2));
            efecto1 = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 2);
            efecto2 = new MobEffectInstance(MobEffects.DIG_SPEED, 999999, 2);
        }
        else if(cuantosItems <= 18) {
            player.removeEffect(efecto1.getEffect());
            player.removeEffect(efecto2.getEffect());
        }
        else if (cuantosItems <= 27) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 999999, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 999999, 1));
            efecto1 = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 999999, 1);
            efecto2 = new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 999999, 1);
        }
        else if (cuantosItems <= 36){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 999999, 3));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 999999, 3));
            efecto1 = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 999999, 3);
            efecto2 = new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 999999, 3);
        }
    }
}
