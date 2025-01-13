import {ArtworkRequest} from "./artworkRequest";
import {Artwork} from "./artwork";
import {User} from "../../user/model/user";

export interface CompleteArtworkRequest {
  artworkRequest: ArtworkRequest,
  artwork: Artwork,
  user: User
}
