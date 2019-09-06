workflow "Build and deploy " {
  on = "push"
  resolves = ["Upload artifact"]
}

action "Compile" {
  uses = "LucaFeger/action-maven-cli@765e218a50f02a12a7596dc9e7321fc385888a27"
  args = "compile"
}

action "Test" {
  uses = "LucaFeger/action-maven-cli@765e218a50f02a12a7596dc9e7321fc385888a27"
  args = "test"
  needs = ["Compile"]
}

action "Upload artifact" {
  uses = "actions/upload-artifact@9da9a3d797c6670a4e5207e787dcd288abfa46f9"
  needs = ["Test"]
}
