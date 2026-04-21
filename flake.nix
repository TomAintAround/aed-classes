{
  description = "AED shell";

  inputs.nixpkgs.url = "github:NixOs/nixpkgs/nixos-unstable";

  outputs = inputs: let
    system = "x86_64-linux";
    pkgs = import inputs.nixpkgs {
      inherit system;
      config.allowUnfree = true;
    };
  in {
    devShells.${system}.default = let
      python =
        pkgs.python313.withPackages
        (ps:
          with ps; [
            debugpy
          ]);
    in
      pkgs.mkShell {
        packages = with pkgs; [python jdk25];
      };
  };
}
