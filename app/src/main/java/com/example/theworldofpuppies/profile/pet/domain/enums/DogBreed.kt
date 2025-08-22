package com.example.theworldofpuppies.profile.pet.domain.enums

enum class DogBreed(val breedName: String) {
    // Sporting Group
    LABRADOR_RETRIEVER("Labrador Retriever"),
    GOLDEN_RETRIEVER("Golden Retriever"),
    GERMAN_SHORT_HAIRED_POINTER("German Shorthaired Pointer"),
    ENGLISH_SPRINGER_SPANIEL("English Springer Spaniel"),
    COCKER_SPANIEL("Cocker Spaniel"),
    BRITTANY("Brittany"),
    WEIMARANER("Weimaraner"),
    VIZSLA("Vizsla"),
    CHESAPEAKE_BAY_RETRIEVER("Chesapeake Bay Retriever"),
    IRISH_SETTER("Irish Setter"),

    // Hound Group
    BEAGLE("Beagle"),
    DACHSHUND("Dachshund"),
    BASSET_HOUND("Basset Hound"),
    BLOODHOUND("Bloodhound"),
    RHODESIAN_RIDGEBACK("Rhodesian Ridgeback"),
    WHIPPET("Whippet"),
    GREYHOUND("Greyhound"),
    AFGHAN_HOUND("Afghan Hound"),
    NORWEGIAN_ELKHOUND("Norwegian Elkhound"),
    AMERICAN_FOXHOUND("American Foxhound"),

    // Working Group
    ROTTWEILER("Rottweiler"),
    BOXER("Boxer"),
    DOBERMAN_PINSCHER("Doberman Pinscher"),
    GREAT_DANE("Great Dane"),
    SIBERIAN_HUSKY("Siberian Husky"),
    ALASKAN_MALAMUTE("Alaskan Malamute"),
    BULLMASTIFF("Bullmastiff"),
    SAINT_BERNARD("Saint Bernard"),
    AKITA("Akita"),
    NEWFOUNDLAND("Newfoundland"),

    // Terrier Group
    STAFFORDSHIRE_BULL_TERRIER("Staffordshire Bull Terrier"),
    AMERICAN_STAFFORDSHIRE_TERRIER("American Staffordshire Terrier"),
    WEST_HIGHLAND_WHITE_TERRIER("West Highland White Terrier"),
    CAIRN_TERRIER("Cairn Terrier"),
    AIREDALE_TERRIER("Airedale Terrier"),
    JACK_RUSSELL_TERRIER("Jack Russell Terrier"),
    SCOTTISH_TERRIER("Scottish Terrier"),
    BORDER_TERRIER("Border Terrier"),
    BULL_TERRIER("Bull Terrier"),
    FOX_TERRIER("Fox Terrier"),

    // Toy Group
    POMERANIAN("Pomeranian"),
    CHIHUAHUA("Chihuahua"),
    SHIH_TZU("Shih Tzu"),
    MALTESE("Maltese"),
    YORKSHIRE_TERRIER("Yorkshire Terrier"),
    PAPILLON("Papillon"),
    CAVALIER_KING_CHARLES_SPANIEL("Cavalier King Charles Spaniel"),
    PEKINGESE("Pekingese"),
    HAVANESE("Havanese"),
    ITALIAN_GREYHOUND("Italian Greyhound"),

    // Non-Sporting Group
    BULLDOG("Bulldog"),
    FRENCH_BULLDOG("French Bulldog"),
    DALMATIAN("Dalmatian"),
    POODLE("Poodle"),
    BOSTON_TERRIER("Boston Terrier"),
    KEESHOND("Keeshond"),
    SHIBA_INU("Shiba Inu"),
    CHOW_CHOW("Chow Chow"),
    AMERICAN_ESKIMO_DOG("American Eskimo Dog"),
    TIBETAN_SPANIEL("Tibetan Spaniel"),

    // Herding Group
    GERMAN_SHEPHERD("German Shepherd"),
    BORDER_COLLIE("Border Collie"),
    AUSTRALIAN_SHEPHERD("Australian Shepherd"),
    PEMBROKE_WELSH_CORGI("Pembroke Welsh Corgi"),
    CARDIGAN_WELSH_CORGI("Cardigan Welsh Corgi"),
    OLD_ENGLISH_SHEEPDOG("Old English Sheepdog"),
    BELGIAN_MALINOIS("Belgian Malinois"),
    BELGIAN_SHEEPDOG("Belgian Sheepdog"),
    SHETLAND_SHEEPDOG("Shetland Sheepdog"),
    COLLIE("Collie"),

    // Misc Popular Breeds
    BASENJI("Basenji"),
    SAMOYED("Samoyed"),
    IRISH_WOLFHOUND("Irish Wolfhound"),
    GREAT_PYRENEES("Great Pyrenees"),
    LEONBERGER("Leonberger"),
    PORTUGUESE_WATER_DOG("Portuguese Water Dog"),
    SOFT_COATED_WHEATEN_TERRIER("Soft Coated Wheaten Terrier"),
    MINIATURE_SCHNAUZER("Miniature Schnauzer"),
    GIANT_SCHNAUZER("Giant Schnauzer"),
    STANDARD_SCHNAUZER("Standard Schnauzer"),
    AUSTRALIAN_CATTLE_DOG("Australian Cattle Dog"),
    ENGLISH_BULLDOG("English Bulldog"),
    AMERICAN_PIT_BULL_TERRIER("American Pit Bull Terrier"),
    DOGUE_DE_BORDEAUX("Dogue de Bordeaux"),
    NEAPOLITAN_MASTIFF("Neapolitan Mastiff"),
    KUVAZ("Kuvasz"),
    KOMONDOR("Komondor"),
    PHARAOH_HOUND("Pharaoh Hound"),
    BORZOI("Borzoi"),
    SALUKI("Saluki"),
    TIBETAN_MASTIFF("Tibetan Mastiff"),
    XL_BULLY("XL Bully"); // newly popular breed

    companion object {
        fun fromName(name: String): DogBreed? {
            return entries.find { it.breedName.equals(name, ignoreCase = true) }
        }
    }

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
            name.first().toString(),
            name.last().toString()
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
