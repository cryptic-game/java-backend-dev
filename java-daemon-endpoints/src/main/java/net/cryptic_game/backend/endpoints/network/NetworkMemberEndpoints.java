package net.cryptic_game.backend.endpoints.network;

import com.google.gson.JsonObject;
import net.cryptic_game.backend.base.api.endpoint.*;
import net.cryptic_game.backend.data.device.Device;
import net.cryptic_game.backend.data.network.Network;
import net.cryptic_game.backend.data.network.NetworkInvitation;
import net.cryptic_game.backend.data.network.NetworkMember;
import net.cryptic_game.backend.data.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NetworkMemberEndpoints extends ApiEndpointCollection {

    public NetworkMemberEndpoints() {
        super("network_member");
    }

    @ApiEndpoint("members")
    public ApiResponse member(@ApiParameter("user_id") final UUID userId,
                              @ApiParameter("device") final UUID deviceId) {
        final User user = User.getById(userId);
        final Device device = Device.getById(deviceId);

        if (!device.isPoweredOn())
            return new ApiResponse(ApiResponseType.FORBIDDEN, "DEVICE_NOT_ONLINE");

        return new ApiResponse(ApiResponseType.OK, NetworkMember.getMembershipsOfDevice(device));

    }

    @ApiEndpoint("request")
    public ApiResponse request(@ApiParameter("user_id") final UUID userId,
                          @ApiParameter("device") final UUID deviceId,
                          @ApiParameter("id") final UUID id) {
        final User user = User.getById(userId);
        final Device device = Device.getById(deviceId);
        final Network network = Network.getById(id);

        if (device == null || !device.hasUserAccess(user)) {
            return new ApiResponse(ApiResponseType.FORBIDDEN, "DEVICE_ACCESS_DENIED");
        }

        if (!device.isPoweredOn()) {
            return new ApiResponse(ApiResponseType.FORBIDDEN, "DEVICE_NOT_ONLINE");
        }

        if (network == null) {
            return new ApiResponse(ApiResponseType.FORBIDDEN, "NETWORK_NOT_FOUND");
        }

        if (NetworkMember.getMember(network, device) != null) {
            return new ApiResponse(ApiResponseType.FORBIDDEN, "ALREADY_MEMBER_OF_NETWORK");
        }

        if (NetworkInvitation.getInvitation(network, device) != null) {
            return new ApiResponse(ApiResponseType.ALREADY_EXISTS, "INVITATION_ALREADY_EXISTS");
        }
        return new ApiResponse(ApiResponseType.OK, NetworkInvitation.createInvitation(network, device, null));
    }

    @ApiEndpoint("invitations")
    public ApiResponse invitations(@ApiParameter("user_id") final UUID user_id,
                              @ApiParameter("device") final UUID device_id,
                              @ApiParameter("id") final UUID id){
        final User user = User.getById(user_id);
        final Device device = Device.getById(device_id);
        final Network network = Network.getById(id);

        if (device == null || !device.hasUserAccess(user)) {
            return new ApiResponse(ApiResponseType.FORBIDDEN, "DEVICE_ACCESS_DENIED");
        }

        if(!device.isPoweredOn()){
            return new ApiResponse(ApiResponseType.FORBIDDEN, "DEVICE_NOT_ONLINE");
        }

        List<JsonObject> invitations = new ArrayList<>();

        return new ApiResponse(ApiResponseType.OK, invitations);
    }

    @ApiEndpoint("leave")
    public ApiResponse leave(@ApiParameter("user_id")final UUID user_id,
                        @ApiParameter("device")final UUID device_id,
                        @ApiParameter("id")final UUID id) {
        final User user = User.getById(user_id);
        final Device device = Device.getById(device_id);
        final Network network = Network.getById(id);

        if (device == null || !device.hasUserAccess(user)) {
            return new ApiResponse(ApiResponseType.FORBIDDEN, "DEVICE_ACCESS_DENIED");
        }

        if(!device.isPoweredOn()) {
            return new ApiResponse(ApiResponseType.FORBIDDEN, "DEVICE_NOT_ONLINE");
        }

        if(network.getOwner().equals(device)){
            return new ApiResponse(ApiResponseType.UNAUTHORIZED, "CANNOT_LEAVE_OWN_NETWORK");
        }
        NetworkMember member = NetworkMember.getMember(network, device);
        if (member != null) {
            member.delete();
            return new ApiResponse(ApiResponseType.OK);
        }
        return new ApiResponse(ApiResponseType.NOT_FOUND, "NETWORK_MEMBER");
    }
}
